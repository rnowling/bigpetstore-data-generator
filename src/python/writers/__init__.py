class TransactionItemWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, trans):
        for item in trans.purchased_items:
            item = dict(item)
            if "food" in item["category"]:
                item_str = "%s:%s:%s:%s" % \
                    (item["category"], item["brand"], item["flavor"], 
                     item["size"])
            elif "poop bags" == item["category"]:
                item_str = "%s:%s:%s:%s" % \
                    (item["category"], item["brand"], item["color"], 
                     item["size"])
            else:
                item_str = "%s:%s:%s" % \
                    (item["category"], item["brand"], item["size"])

            self.fl.write("%s,%s\n" % (trans.transaction_id(),
                                  item_str))
    def close(self):
        self.fl.close()
    

class CustomerWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, customer):
        string = "%s,%s,%s\n" % (customer.id,
                                 customer.name, 
                                 customer.location)
                     
        self.fl.write(string)

    def close(self):
        self.fl.close()

class StoreWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, store):
        string = "%s,%s\n" % (store.id, store.location.zipcode)
        self.fl.write(string)

    def close(self):
        self.fl.close()


class TransactionWriter(object):
    def __init__(self, filename=None):
        self.fl = open(filename, "w")

    def append(self, trans):
            values = [
                trans.transaction_id(),
                trans.store.id,
                trans.customer.id,
                trans.trans_time,
                ]
            string = ",".join(map(str, values)) + "\n"
            self.fl.write(string)

    def close(self):
        self.fl.close()
