import matplotlib
matplotlib.use("PDF")

from matplotlib import pyplot as plt

# When generating output to disk, we'll ignore fields that start with an underscore

DOG_FOOD_FIELDS = {
    "_base_price" : [("base_price", 1.0, 10.79)],
    "_brand" : [("Wellfed", 1.0, 0.0), ("Happy Pup", 1.05, 0.0), ("Dog Days", 1.1, 0.0), ("Chef Corgie", 0.8, 0.0), ("Nature's Cornucopia", 0.9, 0.0)],
    "_meat" : [("Chicken", 1.0, 0.0), ("Pork", 1.0, 0.0), ("Beef", 1.1, 0.0), ("Lamb", 1.2, 0.0), ("Bison", 1.2, 0.0), ("Venison", 1.2, 0.0), ("Rabbit", 1.1, 0.0), ("Salmon", 1.2, 0.0), ("Vegetarian", 1.0, 0.0)],
    "_grain" : [("Potato", 1.0, 0.0), ("Sweet Potato", 1.0, 0.0), ("Rice", 1.0, 0.0), ("Brown Rice", 1.0, 0.0), ("Soy", 1.0, 0.0), ("Barley", 1.0, 0.0)],
    "quantity" : [(4.5, 1.0, 0.0), (15.0, 2.50, 0.0), (30.0, 3.69, 0.0)],
    "_limited_ingredient" : [(True, 1.0, 1.25), (False, 1.0, 0.0)],
    "_age" : [("Puppy", 1.0, 0.0), ("Senior", 1.0, 0.0), ("Adult", 1.0, 0.0)],
    "_weight_loss" : [(True, 1.0, 0.0), (False, 1.0, 0.0)],
    "_organic" : [(True, 1.0, 2.50), (False, 1.0, 0.0)]
}

def combination_generator(field_name, choices, previous=None):
    if previous == None:
        for choice in choices:
            d = dict()
            d[field_name] = choice
            yield d
    else:
        for previous_state in previous:
            for choice in choices:
                previous_state = previous_state.copy()
                previous_state[field_name] = choice
                yield previous_state

# internal vs external fields
# limiting values based on other values e.g, limited ingredient for only certain brands

def generate_dog_food(food_fields):
    field_names = food_fields.keys()
    generator = None
    for name in field_names:
        generator = combination_generator(name, food_fields[name], previous=generator)
    items = [item for item in generator]

    processed_items = []
    for item in items:
        processed_item = dict()
        multipliers = 1.0
        additions = 0.0
        for field_name, (value, multiplier, addition) in item.iteritems():
            multipliers *= multiplier
            additions += addition
            # drop base_price
            if field_name == "_base_price":
                continue
            processed_item[field_name] = value
        price = multipliers * additions

        # filter out certain combinations
        if processed_item["_brand"] == "Chef Corgie":
            if processed_item["_organic"]:
                continue
            if processed_item["_limited_ingredient"]:
                continue
            if processed_item["_meat"] == "Venison":
                continue
            if processed_item["_meat"] == "Bison":
                continue
            if processed_item["_meat"] == "Rabbit":
                continue
            if processed_item["_meat"] == "Salmon":
                continue
            if processed_item["_meat"] == "Vegetarian":
                continue
            if processed_item["_grain"] == "Soy":
                continue
            if processed_item["_grain"] == "Sweet Potato":
                continue
            if processed_item["_grain"] == "Brown Rice":
                continue

        if processed_item["_brand"] == "Nature's Cornucopia":
            if processed_item["_organic"]:
                continue
            if processed_item["_limited_ingredient"]:
                continue
            if processed_item["_meat"] == "Venison":
                continue
            if processed_item["_meat"] == "Bison":
                continue
            if processed_item["_meat"] == "Rabbit":
                continue
            if processed_item["_grain"] == "Sweet Potato":
                continue
            if processed_item["_grain"] == "Brown Rice":
                continue

        processed_item["price"] = round(price,2)
        processed_item["_per_unit_cost"] = price / processed_item["quantity"]

        desc = processed_item["_brand"]
        if processed_item["_organic"]:
            desc += " Organic"
        if processed_item["_limited_ingredient"]:
            desc += " Limited Ingredient"
        desc += " %s & %s" % (processed_item["_meat"], processed_item["_grain"])
        if processed_item["_weight_loss"]:
            desc += " Weight Loss Formula"
        if processed_item["_age"] == "senior":
            desc += " for Seniors"
        else:
            desc += " for Puppies"
        processed_item["description"] = desc

        yield processed_item


if __name__ == "__main__":
    fl = open("products.txt", "w")
    items = generate_dog_food(DOG_FOOD_FIELDS)
    for item in items:
        fl.write("%s,%s,%s\n" % (item["description"], item["quantity"], item["price"]))
    fl.close()
               
    #price_list = [item["price"] for item in items]
    #print "Number of items:", len(price_list)
    #plt.hist(price_list)
    #plt.savefig("prices.pdf")
    
    
    
        
