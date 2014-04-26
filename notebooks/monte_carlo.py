import random

class MonteCarloGenerator(object):
	def __init__(self, generator, pdf):
		self.generator = generator
		self.pdf = pdf
		self.iterations = 0
		self.num_accepted = 0

	def generate(self):
		while True:
			self.iterations += 1
			obj = self.generator.generate()
			prob = self.pdf.probability(obj)
			rand = random.random()
			try:
				if rand < prob:
					self.num_accepted += 1
					return obj
			except:
				print type(prob), prob

	def acceptance_rate(self):
		if self.num_accepted is 0:
			return 0.0
		return float(self.num_accepted) / float(self.iterations)
