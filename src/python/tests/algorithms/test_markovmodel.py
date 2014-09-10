import random
import unittest

from algorithms.markovmodel import MarkovModel
from algorithms.markovmodel import MarkovProcess
from algorithms.markovmodel import MarkovModelBuilder


class TestMarkovModelBuilder(unittest.TestCase):
    def test_add_start_state(self):
        builder = MarkovModelBuilder()

        builder.add_start_state("a", 1.0)

        self.assertIn("a", builder.start_states)
        self.assertEqual(builder.start_states["a"], 1.0)

    def test_add_edge_weight(self):
        builder = MarkovModelBuilder()

        builder.add_edge_weight("a", "b", 1)
        
        self.assertIn("a", builder.edge_weights)
        self.assertIn("b", builder.edge_weights["a"])
        self.assertEqual(1, builder.edge_weights["a"]["b"])

    def test_compute_transition_probabilities(self):
        builder = MarkovModelBuilder()

        builder.add_start_state("a", 1.0)
        builder.add_edge_weight("a", "b", 1)
        builder.add_edge_weight("a", "c", 1)
        
        edge_prob = builder.compute_transition_probabilities()

        self.assertIn("a", edge_prob)
        self.assertIn("b", edge_prob["a"])
        self.assertIn("c", edge_prob["a"])

        self.assertTrue(abs(0.5 - edge_prob["a"]["b"]) < 0.0001)
        self.assertTrue(abs(0.5 - edge_prob["a"]["c"]) < 0.0001)


    def test_build_msm(self):
        builder = MarkovModelBuilder()

        builder.add_start_state("a", 1.0)
        builder.add_edge_weight("a", "b", 1)
        builder.add_edge_weight("a", "c", 1)

        msm = builder.build_msm()
        
        self.assertIsInstance(msm, MarkovModel)

        self.assertIn("a", msm.start_states)

        edge_prob = msm.edge_probabilities

        self.assertIn("a", edge_prob)
        self.assertIn("b", edge_prob["a"])
        self.assertIn("c", edge_prob["a"])

        self.assertTrue(abs(0.5 - edge_prob["a"]["b"]) < 0.0001)
        self.assertTrue(abs(0.5 - edge_prob["a"]["c"]) < 0.0001)

class TestMarkovProcess(unittest.TestCase):
    def test_init(self):
        start_states = {"a" : 1.0}
        edge_prob = {"a" : {"b" : 0.5, "c" : 0.5}}

        msm = MarkovModel(start_states, edge_prob)
        process = MarkovProcess(msm)
        
        self.assertIsInstance(msm.start_states, dict)
        self.assertIn("a", msm.start_states)
        self.assertEqual(process.current_state, "a")

    def test_progress_state(self):
        start_states = {"a" : 1.0}
        edge_prob = {"a" : {"b" : 0.5, "c" : 0.5}}

        model = MarkovModel(start_states, edge_prob)

        process = MarkovProcess(model)
        
        new_state = process.progress_state()

        self.assertIn(new_state, ["b", "c"])

        self.assertRaises(Exception, process.progress_state)
        

