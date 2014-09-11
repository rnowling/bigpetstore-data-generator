class PurchasingProfile(object):
    """
    Stores Markov models modeling a customer's purchasing for
    each product category.
    """
    def __init__(self, product_category_profiles):
        """
        product_category_profiles -- dict of product categories (string)
        to MarkovModels
        """
        self.product_category_profiles = product_category_profiles

    def get_profile(self, product_category):
        """
        Get the MarkovModel for product_category
        """
        return self.product_category_profiles[product_category]

    def get_product_categories(self):
        """
        Get a list of all product category names
        """
        return self.product_category_profiles.keys()        
