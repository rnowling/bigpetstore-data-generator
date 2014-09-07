class Store(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.zipcode = None
        self.coords = None

    def __repr__(self):
        return "%s,%s,%s" % (self.name, self.zipcode, self.coords)

class Customer(object):
    def __init__(self):
        self.id = None
        self.name = None
        self.location = None
        self.average_transaction_trigger_time = None
        self.average_purchase_trigger_time = None
        self.pets = {
                        "dog" : 0,
                        "cat" : 0
                    }

    def __repr__(self):
        return "(%s, %s, %s dogs, %s cats, %s)" % \
            (self.id, self.name, self.pets["dog"], 
             self.pets["cat"], self.location)
