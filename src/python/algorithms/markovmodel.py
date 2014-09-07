from collections import defaultdict

import random

class MarkovModelBuilder(object):
    def __init__(self):
        self.states = set()
        self.edge_weights = defaultdict(lambda: defaultdict(lambda: 0))

    def add_state(self, state_label):
        self.states.add(state_label)

    def add_edge_weight(self, start_state, end_state, weight):
        self.edge_weights[start_state][end_state] = weight

    def compute_transition_probabilities(self):
        edge_probabilities = defaultdict(lambda: defaultdict(lambda: 0))
        for start_state in self.states:
            weight_sum = 0.0
            for end_state, weight in self.edge_weights[start_state].iteritems():
                weight_sum += weight
            for end_state, weight in self.edge_weights[start_state].iteritems():
                edge_probabilities[start_state][end_state] = weight / weight_sum
        return edge_probabilities

    def build_msm(self):
        edge_probabilities = self.compute_transition_probabilities()
        return MarkovModel(states=self.states, edge_probabilities=edge_probabilities)


class MarkovModel(object):
    def __init__(self, states=None, edge_probabilities=None):
        self.states = list(states)
        self.edge_probabilities = edge_probabilities
        self.current_state = random.choice(self.states)

    def progress_state(self):
        r = random.random()
        cum_sum = 0.0
        for candidate_state, prob in self.edge_probabilities[self.current_state].iteritems():
            cum_sum += prob
            if r <= cum_sum:
                self.current_state = candidate_state
                return candidate_state
        raise Exception, "Could not find next state for Markov Model!"
