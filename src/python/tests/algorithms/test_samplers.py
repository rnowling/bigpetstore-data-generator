import random
import unittest

from algorithms.samplers import BoundedMultiModalGaussianSampler
from algorithms.samplers import RouletteWheelSampler

class TestRouletteWheelSampler(unittest.TestCase):
    def test_sample(self):
        data_points = [("a", 0.25), ("b", 0.25), ("c", 0.25), ("d", 0.25)]

        sampler = RouletteWheelSampler(data_points)

        result1 = sampler.sample()

        self.assertIn(result1, ["a", "b", "c", "d"])

class TestBoundedMultiModalGaussianSampler(unittest.TestCase):
    def test_sample(self):
        distr = [(0.25, 0.1), (0.75, 0.1)]
        
        sampler = BoundedMultiModalGaussianSampler(distr)

        sample = sampler.sample()

        self.assertIsInstance(sample, float)

    def test_sample_bounds(self):
        distr = [(-100, 0.1), (-50, 0.1)]
        bounds = (0.0, 1.0)
        
        sampler = BoundedMultiModalGaussianSampler(distr, bounds=bounds)

        sample = sampler.sample()

        self.assertIsInstance(sample, float)
        self.assertTrue(sample >= 0.0)
        self.assertTrue(sample <= 1.0)

        
