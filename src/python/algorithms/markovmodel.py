from algorithms.samplers import RouletteWheelSampler

import random

class MarkovModelBuilder(object):
    """
    MarkovModelBuilder provides a high-level API for building
    MarkovModels.

    Example:

    >>> builder = MarkovModelBuilder()
    >>> builder.add_start_state("a", 1.0)
    >>> builder.add_edge_weight("a", "b", 0.5)
    >>> builder.add_edge_weight("a", "c", 0.5)
    >>> model = builder.build_msm()

    """
    def __init__(self):
        self.start_states = dict()
        self.edge_weights = dict()

    def add_start_state(self, state_label, weight):
        """
        Add the given state to the list of start states
        with the given weight.

        The weights will be normalized before constructing
        the MarkovModel so the input weights do not need
        to be.
        """
        self.start_states[state_label] = weight

    def add_edge_weight(self, start_state, end_state, weight):
        """
        Add a transition from start_state to end_state. By
        default all state transitions have a probability of 0.

        This method also adds the states to the model if they
        don't already exist.

        The weights are normalized when the model is created.
        """
        if start_state not in self.edge_weights:
            self.edge_weights[start_state] = dict()
        if end_state not in self.edge_weights[start_state]:
            self.edge_weights[start_state][end_state] = dict()
        self.edge_weights[start_state][end_state] = weight

    def compute_transition_probabilities(self):
        edge_probabilities = dict()
        for start_state in self.edge_weights.iterkeys():
            edge_probabilities[start_state] = dict()
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
    """
    Stores transition matrix and start state probabilities
    for all of the states in the Markov Model.

    start_states is expected to be a dictionary of state
    labels to probabilities.

    edge_probabilities is a dict of a dict giving the 
    transition probabilities.

    e.g., edge_probabilities[start_state][end_state] = prob
    """
    def __init__(self, start_states, edge_probabilities):
        self.start_states = start_states
        self.edge_probabilities = edge_probabilities

class MarkovProcess(object):
    """
    Simulate a Markov process on the given Markov model.

    Markov processes are distinct from Markov models to
    allow a single Markov model to be used for simulating
    multiple Markov processes. Construction of Markov models
    can be computationally expensive but simulating Markov
    processes is cheap.
    """

    def __init__(self, markov_model):
        """
        Initializes the Markov process by choosing an initial 
        state according to the probabilities in the Markov
        model.
        """
        self.markov_model = markov_model
        start_states = list(markov_model.start_states.items())
        self.current_state = RouletteWheelSampler(start_states).sample()

    def progress_state(self):
        """
        Choose the next state in the Markov process according to
        the transition matrix.  Return the previous state.
        """
        r = random.random()
        cum_sum = 0.0
        for candidate_state, prob in self.markov_model.edge_probabilities[self.current_state].iteritems():
            cum_sum += prob
            if r <= cum_sum:
                self.current_state = candidate_state
                return candidate_state
        raise Exception, "Could not find next state for Markov Model!"
