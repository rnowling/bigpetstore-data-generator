class PurchasingProfile(object):
    def __init__(self, product_category_profiles):
        self.product_category_profiles = product_category_profiles

    def get_profile(self, product_category):
        return self.product_category_profiles[product_category]

    def get_product_categories(self):
        return self.product_category_profiles.keys()        
