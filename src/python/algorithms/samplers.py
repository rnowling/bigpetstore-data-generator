import math
import random

class RouletteWheelSampler(object):
    def __init__(self, values):
        self._wheel = []
        end = 0.0
        for x, w in values:
            end += w
            self._wheel.append((end, x))

    def sample(self):
        r = random.random()
        for end, x in self._wheel:
            if r <= end:
                return x
        # we should never get here since probabilities
        # should sum to 1
        raise Exception, "Could not pick a value!"

class BoundedMultiModalGaussianSampler(object):
    def __init__(self, distributions, bounds=None):
        self.distributions = distributions
        self.bounds = bounds

    def sample(self):
        mean, var = random.choice(self.distributions)
        sample = random.normalvariate(mean, math.sqrt(var))
        if self.bounds is not None:
            sample = max(self.bounds[0], min(self.bounds[1], sample))
        return sample
