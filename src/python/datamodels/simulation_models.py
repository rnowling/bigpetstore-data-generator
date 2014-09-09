from algorithms.markovmodel import MarkovProcess

class PurchasingProfile(object):
    def __init__(self):
        self.product_category_profiles = dict()

    def add_profile(self, product_category, markov_model):
        self.product_category_profiles[product_category] = markov_model

    def get_profile(self, product_category):
        return self.product_category_profiles[product_category]

    def get_product_categories(self):
        return self.product_category_profiles.keys()

    def build_processes(self):
        processes = dict()
        for category, model in self.product_category_profiles.iteritems():
            process = MarkovProcess(model)
            processes[category] = process
        return PurchasingProcesses(processes)

class PurchasingProcesses(object):
    def __init__(self, product_processes):
        self.product_processes = product_processes

    def simulate_purchase(self, product_category):
        return self.product_processes[product_category].progress_state()
