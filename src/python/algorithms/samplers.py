import math
import random

class RouletteWheelSampler(object):
    """
    Simple sampler used for sampling from a discrete set
    of weighted items.
    """
    def __init__(self, values):
        """
        Initialize the sampler.

        Takes a list of 2-tuples each containing an item
        and its weight.  The weights are normalized to
        find the probability mass function of the items.
        """
        self._wheel = []
        
        values = self._normalize(values)

        end = 0.0
        for x, w in values:
            end += w
            self._wheel.append((end, x))

    def _normalize(self, values):
        """
        Takes a list of 2-tuples each containing an item
        and its weight.  The weights are normalized to
        find the probability mass function of the items.

        Returns a list of 2-tuples containing an item
        and its probability.
        """
        weight_sum = 0.0
        for x, w in values:
            weight_sum += w

        normalized = []
        for x, w in values:
            normalized.append((x, w / weight_sum))

        return normalized

    def sample(self):
        """
        Randomly choose an item from the set and
        return it.
        """
        r = random.random()
        for end, x in self._wheel:
            if r <= end:
                return x
        # we should never get here since probabilities
        # should sum to 1
        raise Exception, "Could not pick a value!"

class BoundedMultiModalGaussianSampler(object):
    """
    Sampler for sampling from a set of Gaussian distributions
    where the domain is bounded.

    For example, we can sample from a pair of Gaussians with
    means of 0.25 and 0.75, variances of 0.1, and bounded
    between 0 and 1.

    Note: this is not mathematically accurate.  Values below
    or above the bounds are rounded to the bounds and each
    Gaussian is chosen with equal probability instead of
    modeling a true multimodal bounded Guassian.
    """
    def __init__(self, distributions, bounds=None):
        """
        distributions -- list of 2-tuples containing the mean
        and variance of each Gaussian distribution.

        bounds -- (optional) 2-tuple giving the minimum and
        maximum bounds.
        """
        self.distributions = distributions
        self.bounds = bounds

    def sample(self):
        """
        Samples a value.

        Chooses one of the Gaussian distributions.  Samples from
        the distribution.  Rounds the result to the bounds, if given.
        """
        mean, var = random.choice(self.distributions)
        sample = random.normalvariate(mean, math.sqrt(var))
        if self.bounds is not None:
            sample = max(self.bounds[0], min(self.bounds[1], sample))
        return sample
