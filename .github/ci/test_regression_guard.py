import tempfile
from pathlib import Path
import unittest

from check_training import compare, read_cases


class RegressionGuardTest(unittest.TestCase):
    def setUp(self):
        self.key = ('app/build/test-results/test/TEST-Lesson.xml', 'Lesson', 'exercise')
        self.baseline = {'cases': [{'id': list(self.key), 'failure_type': 'AssertionError'}]}

    def test_existing_failure_is_preserved(self):
        self.assertEqual(compare(self.baseline, {self.key: 'AssertionError'}, 1)['new_failures'], 0)

    def test_existing_failure_may_be_fixed(self):
        self.assertEqual(compare(self.baseline, {self.key: None}, 0)['baseline_failures_now_passing'], 1)

    def test_new_failure_is_rejected(self):
        with self.assertRaises(ValueError):
            compare(self.baseline, {self.key: 'AssertionError', ('new', 'new', 'new'): 'AssertionError'}, 1)

    def test_changed_failure_is_rejected(self):
        with self.assertRaises(ValueError):
            compare(self.baseline, {self.key: 'NullPointerException'}, 1)

    def test_removed_test_is_rejected(self):
        with self.assertRaises(ValueError):
            compare(self.baseline, {}, 0)

    def test_infrastructure_exit_is_rejected(self):
        for code in [0, 2, -9]:
            with self.subTest(code=code), self.assertRaises(ValueError):
                compare(self.baseline, {self.key: 'AssertionError'}, code)

    def test_missing_or_empty_xml_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            repo = Path(directory)
            with self.assertRaises(ValueError):
                read_cases(repo)
            report = repo / self.key[0]
            report.parent.mkdir(parents=True)
            report.write_text('<testsuite/>')
            with self.assertRaises(ValueError):
                read_cases(repo)

    def test_skip_error_and_duplicate_are_rejected(self):
        for child in ['<skipped/>', '<error type="InfrastructureError"/>']:
            with self.subTest(child=child), tempfile.TemporaryDirectory() as directory:
                repo = Path(directory)
                report = repo / self.key[0]
                report.parent.mkdir(parents=True)
                report.write_text('<testsuite><testcase classname="Lesson" name="exercise">' + child + '</testcase></testsuite>')
                with self.assertRaises(ValueError):
                    read_cases(repo)
        with tempfile.TemporaryDirectory() as directory:
            repo = Path(directory)
            report = repo / self.key[0]
            report.parent.mkdir(parents=True)
            report.write_text('<testsuite>' + '<testcase classname="Lesson" name="exercise"/>' * 2 + '</testsuite>')
            with self.assertRaises(ValueError):
                read_cases(repo)


if __name__ == '__main__':
    unittest.main()
