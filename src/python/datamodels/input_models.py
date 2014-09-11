import math

class Names(object):
    """
    First names, last names, and their corresponding
    weights.
    """

    def __init__(self, first_names, last_names):
        """
        first_names -- list of 2-tuples containing a first
        name and its corresponding weight.

        last_names -- list of 2-tuples containing a last
        name and its corresponding weight.
        """
        self.first_names = first_names
        self.last_names = last_names

class ZipcodeRecord(object):
    """
    Zipcode, its median household income, population, and GPS
    coordinates.
    """

    def __init__(self, zipcode=None, median_household_income=None,
                 population=None, coords=None):
        """
        zipcode -- string containing zipcode

        median_household_income -- number

        population -- number

        coords -- 2-tuple of the latitude and longitude of the zipcode
        """
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
    """
    Stores the products in a given product category as well
    as various details about the category itself.
    """
    def __init__(self, category=None, fields=None, items=None, \
            species=None, trigger_transaction=False,
            daily_usage_rate=None, base_amount_used_average=None,
            base_amount_used_variance=None,
            transaction_trigger_rate=None,
            transaction_purchase_rate=None):
        """
        category -- string giving the name of the category

        fields -- list of strings giving the fields of the products

        items -- list of products stored as dicts containing the
        field names and values

        species -- list of strings giving the species that use
        the products

        trigger_transaction -- boolean indicating whether 
        consumption of the item triggers a transaction

        daily_usage_rate -- number of times the item is used
        per day

        base_amount_used_average -- average amount used per pet per usage

        base_amount_used_variance -- variance of the amount used per pet per usage

        transaction_trigger_rate -- 

        transaction_purchase_rate --
        """
    
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
