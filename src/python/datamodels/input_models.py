class ZipcodeRecord(object):
    def __init__(self, zipcode=None, median_household_income=None,
                 population=None, coords=None):
        self.zipcode = zipcode
        self.median_household_income = median_household_income
        self.population = population
        self.coords = coords

class ProductCategory(object):
    def __init__(self, category=None, fields=None, items=None, \
            species=None, trigger_transaction=False,
            daily_usage_rate=None, base_amount_used_average=None,
            base_amount_used_variance=None,
            transaction_trigger_rate=None,
            transaction_purchase_rate=None):
    
        self.category_label = category
        self.fields = fields
        self.items = items
        self.species = species
        self.trigger_transaction = trigger_transaction
        self.daily_usage_rate = daily_usage_rate
        self.base_amount_used_average = base_amount_used_average
        self.base_amount_used_variance = base_amount_used_average
        self.transaction_trigger_rate = transaction_trigger_rate
        self.transaction_purchase_rate = transaction_purchase_rate
