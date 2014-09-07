import random
import unittest

from algorithms.samplers import RouletteWheelSampler

class RouletteWheelSamplerTests(unittest.TestCase):
    def test_sample(self):
        data_points = [("a", 0.25), ("b", 0.25), ("c", 0.25), ("d", 0.25)]

        sampler = RouletteWheelSampler(data_points)

        result1 = sampler.sample()

        self.assertIn(result1, ["a", "b", "c", "d"])

    def test_non_normalized(self):
        data_points = [("a", 0.001), ("b", 0.001)]

        random_state = random.getstate()
        random.seed(456)

        sampler = RouletteWheelSampler(data_points)

        self.assertRaises(Exception, sampler.sample)

        random.setstate(random_state)
