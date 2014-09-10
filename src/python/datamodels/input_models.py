import math

class Names(object):
    def __init__(self, first_names, last_names):
        self.first_names = first_names
        self.last_names = last_names

class ZipcodeRecord(object):
    def __init__(self, zipcode=None, median_household_income=None,
                 population=None, coords=None):
        self.zipcode = zipcode
        self.median_household_income = median_household_income
        self.population = population
        self.coords = coords

    def distance(self, other_record):
        """
        Computes distance between two zipcodes in miles using
        the latitude-longitude pairs.
        """
        if other_record.zipcode == self.zipcode:
            return 0.0

        lat_A, long_A = other_record.coords
        lat_B, long_B = self.coords

        dist = (math.sin(math.radians(lat_A)) *
                math.sin(math.radians(lat_B)) +
                math.cos(math.radians(lat_A)) *
                math.cos(math.radians(lat_B)) *
                math.cos(math.radians(long_A - long_B)))
        dist = (math.degrees(math.acos(dist))) * 69.09

        return dist


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
