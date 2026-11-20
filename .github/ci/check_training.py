"""Detect regressions without changing exercises or exposing assertion answers."""
import json
import os
from pathlib import Path
import subprocess
import tempfile
import xml.etree.ElementTree as ET


def read_cases(repo):
    reports = sorted(repo.glob('**/build/test-results/test/TEST-*.xml'))
    if not reports:
        raise ValueError('No JUnit reports were produced')
    cases = {}
    for report in reports:
        suite = ET.parse(report).getroot()
        for case in suite.iter('testcase'):
            key = (report.relative_to(repo).as_posix(), case.get('classname'), case.get('name'))
            if key in cases:
                raise ValueError('Duplicate test identity')
            if case.find('skipped') is not None or case.find('error') is not None:
                raise ValueError('A test was skipped or had an execution error')
            failure = case.find('failure')
            cases[key] = None if failure is None else failure.get('type', 'unknown')
    if not cases:
        raise ValueError('JUnit reports contained no tests')
    return cases


def compare(baseline, current, exit_code):
    expected = {tuple(case['id']): case['failure_type'] for case in baseline['cases']}
    if not expected or len(expected) != len(baseline['cases']):
        raise ValueError('Invalid baseline')
    if expected.keys() - current.keys():
        raise ValueError('Previously executed tests are missing')
    failed = {key: kind for key, kind in current.items() if kind is not None}
    if any(key not in expected or expected[key] != kind for key, kind in failed.items()):
        raise ValueError('A new or changed test failure was detected')
    if exit_code not in (0, 1) or (exit_code == 0) != (not failed):
        raise ValueError('Gradle exit status does not match the test results')
    return {'tests': len(current), 'existing_failures': len(failed), 'new_failures': 0,
            'baseline_failures_now_passing': sum(kind is not None and current[key] is None
                                                 for key, kind in expected.items())}


def main():
    repo = Path(__file__).resolve().parents[2]
    baseline = json.loads((Path(__file__).with_name('baseline.json')).read_text())
    summary = {'baseline_commit': baseline['commit'], 'source_commit': os.environ.get('GITHUB_SHA'),
               'status': 'failed'}
    destination = repo / 'build/ci/training-summary.json'
    try:
        # Capture raw compiler/test output privately: assertion output can reveal exercise answers.
        with tempfile.TemporaryFile() as log:
            gradle = ['bash', './gradlew', '--no-daemon', '--console=plain', '-Dorg.gradle.jvmargs=-Xmx512m']
            built = subprocess.run(gradle + ['clean', 'assemble', 'testClasses'], cwd=repo,
                                   stdout=log, stderr=subprocess.STDOUT)
            if built.returncode:
                raise ValueError('Compilation or assembly failed; reproduce locally for diagnostics')
            tested = subprocess.run(gradle + ['test'], cwd=repo, stdout=log, stderr=subprocess.STDOUT)
            summary.update(compare(baseline, read_cases(repo), tested.returncode))
        summary['status'] = 'passed'
        print('Regression check passed: {tests} tests, {existing_failures} existing failures, '
              '{new_failures} new failures.'.format(**summary))
        return 0
    except (ValueError, ET.ParseError, OSError) as exc:
        # Do not print exception contents from XML, subprocesses or user exercise code.
        summary['reason'] = str(exc) if type(exc) is ValueError else 'Unable to read test results or run the build'
        print('Regression check failed: ' + summary['reason'])
        return 1
    finally:
        destination.parent.mkdir(parents=True, exist_ok=True)
        destination.write_text(json.dumps(summary, indent=2) + '\n', encoding='utf-8')


if __name__ == '__main__':
    raise SystemExit(main())
