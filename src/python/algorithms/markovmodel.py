from collections import defaultdict

from algorithms.samplers import RouletteWheelSampler

import random

class MarkovModelBuilder(object):
    def __init__(self):
        self.start_states = dict()
        self.edge_weights = defaultdict(lambda: defaultdict(lambda: 0))

    def add_start_state(self, state_label, weight):
        self.start_states[state_label] = weight

    def add_edge_weight(self, start_state, end_state, weight):
        self.edge_weights[start_state][end_state] = weight

    def compute_transition_probabilities(self):
        edge_probabilities = defaultdict(lambda: defaultdict(lambda: 0))
        for start_state in self.edge_weights.iterkeys():
            weight_sum = 0.0
            for end_state, weight in self.edge_weights[start_state].iteritems():
                weight_sum += weight
            for end_state, weight in self.edge_weights[start_state].iteritems():
                edge_probabilities[start_state][end_state] = weight / weight_sum
        return edge_probabilities

    def build_msm(self):
        edge_probabilities = self.compute_transition_probabilities()
        return MarkovModel(start_states=self.start_states.copy(), edge_probabilities=edge_probabilities)

class MarkovModel(object):
    def __init__(self, start_states, edge_probabilities):
        self.start_states = start_states
        self.edge_probabilities = edge_probabilities

class MarkovProcess(object):
    def __init__(self, markov_model):
        self.markov_model = markov_model
        start_states = list(markov_model.start_states.items())
        self.current_state = RouletteWheelSampler(start_states).sample()

    def progress_state(self):
        r = random.random()
        cum_sum = 0.0
        for candidate_state, prob in self.markov_model.edge_probabilities[self.current_state].iteritems():
            cum_sum += prob
            if r <= cum_sum:
                self.current_state = candidate_state
                return candidate_state
        raise Exception, "Could not find next state for Markov Model!"
