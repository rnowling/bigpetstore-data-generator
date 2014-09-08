class PurchasingProfile(object):
    def __init__(self):
        self.product_category_profiles = dict()

    def add_profile(self, product_category, markov_model):
        self.product_category_profiles[product_category] = markov_model

    def get_profile(self, product_category):
        return self.product_category_profiles[product_category]

    def get_product_categories(self):
        return self.product_category_profiles.keys()
