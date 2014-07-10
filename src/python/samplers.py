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
			if r < end:
				return x
		# we should never get here since probabilities
		# should sum to 1
		raise Exception, "Could not pick a value!"