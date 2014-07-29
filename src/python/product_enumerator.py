import matplotlib
matplotlib.use("PDF")

from matplotlib import pyplot as plt

from collections import defaultdict
import json

# When generating user output, we'll ignore fields that start with an underscore

DRY_DOG_FOOD_FIELDS = {
    "_base_price" : [("base_price", 1.0, 10.79)],
    "_brand" : [("Wellfed", 1.0, 0.0), ("Happy Pup", 1.05, 0.0), ("Dog Days", 1.1, 0.0), ("Chef Corgie", 0.8, 0.0), ("Nature's Cornucopia", 0.9, 0.0)],
    "_meat" : [("Chicken", 1.0, 0.0), ("Pork", 1.0, 0.0), ("Beef", 1.1, 0.0), ("Lamb", 1.2, 0.0), ("Bison", 1.2, 0.0), ("Venison", 1.2, 0.0), ("Rabbit", 1.1, 0.0), ("Salmon", 1.2, 0.0), ("Vegetarian", 1.0, 0.0)],
    "_grain" : [("Potato", 1.0, 0.0), ("Sweet Potato", 1.0, 0.0), ("Rice", 1.0, 0.0), ("Brown Rice", 1.0, 0.0), ("Soy", 1.0, 0.0), ("Barley", 1.0, 0.0), ("Corn", 1.0, -0.25)],
    "quantity" : [(4.5, 1.0, 0.0), (15.0, 2.50, 0.0), (30.0, 3.69, 0.0)],
    "_limited_ingredient" : [(True, 1.0, 1.25), (False, 1.0, 0.0)],
    "_age" : [("Puppy", 1.0, 0.0), ("Senior", 1.0, 0.0), ("Adult", 1.0, 0.0)],
    "_weight_loss" : [(True, 1.0, 0.0), (False, 1.0, 0.0)],
    "_organic" : [(True, 1.0, 2.50), (False, 1.0, 0.0)]
}

DRY_CAT_FOOD_FIELDS = {
    "_base_price" : [("base_price", 1.0, 12.99)],
    "_brand" : [("Wellfed", 1.0, 0.0), ("Pretty Cat", 1.05, 0.0), ("Feisty Feline", 1.1, 0.0), ("Nature's Cornucopia", 0.9, 0.0)],
    "_meat" : [("Chicken", 1.0, 0.0), ("Tuna", 1.0, 0.0), ("Turkey", 1.1, 0.0), ("Salmon", 1.2, 0.0)],
    "_grain" : [("Potato", 1.0, 0.0), ("Rice", 1.0, 0.0), ("Brown Rice", 1.0, 0.0), ("Corn", 1.0, -0.25)],
    "quantity" : [(3.5, 1.0, 0.0), (7.0, 1.70, 0.0), (15.0, 2.92, 0.0)],
    "_limited_ingredient" : [(True, 1.0, 1.25), (False, 1.0, 0.0)],
    "_age" : [("Kitten", 1.0, 0.0), ("Senior", 1.0, 0.0), ("Adult", 1.0, 0.0)],
    "_hairball_management" : [(True, 1.0, 0.0), (False, 1.0, 0.0)],
    "_organic" : [(True, 1.0, 2.50), (False, 1.0, 0.0)]
}


def generate_combinations(field_name, choices, previous=None):
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

def generate_items(food_fields):
    field_names = food_fields.keys()
    generator = None
    for name in field_names:
        generator = generate_combinations(name, food_fields[name], previous=generator)
    items = [item for item in generator]

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

        processed_item["price"] = round(price,2)
        processed_item["_per_unit_cost"] = price / processed_item["quantity"]

        yield processed_item


# internal vs external fields
# limiting values based on other values e.g, limited ingredient for only certain brands

def generate_dog_food(food_fields):
    for processed_item in generate_items(food_fields):
        # filter out certain combinations
        if processed_item["_grain"] == "Corn":
            if processed_item["_limited_ingredient"]:
                continue
            if processed_item["_brand"] == "Happy Pup":
                continue            
            if processed_item["_brand"] == "Dog Days":
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
            if processed_item["_grain"] == "Sweet Potato":
                continue
            if processed_item["_grain"] == "Brown Rice":
                continue

        desc = processed_item["_brand"]
        if processed_item["_organic"]:
            desc += " Organic"
        if processed_item["_limited_ingredient"]:
            desc += " Limited Ingredient"
        desc += " %s & %s" % (processed_item["_meat"], processed_item["_grain"])
        if processed_item["_weight_loss"]:
            desc += " Weight Loss Formula"
        if processed_item["_age"] == "Senior":
            desc += " for Seniors"
        elif processed_item["_age"] == "Puppy":
            desc += " for Puppies"
        processed_item["description"] = desc

        yield processed_item

def generate_cat_food(food_fields):
    for processed_item in generate_items(food_fields):
        # filter out certain combinations
        if processed_item["_grain"] == "Corn":
            if processed_item["_limited_ingredient"]:
                continue
            if processed_item["_brand"] == "Pretty Cat":
                continue            
            if processed_item["_brand"] == "Feisty Feline":
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

        desc = processed_item["_brand"]
        if processed_item["_organic"]:
            desc += " Organic"
        if processed_item["_limited_ingredient"]:
            desc += " Limited Ingredient"
        desc += " %s & %s" % (processed_item["_meat"], processed_item["_grain"])
        if processed_item["_hairball_management"]:
            desc += " Hairball Management Formula"
        if processed_item["_age"] == "Senior":
            desc += " for Seniors"
        elif processed_item["_age"] == "Kitten":
            desc += " for Kittens"
        processed_item["description"] = desc

        yield processed_item


def main():
    products = defaultdict(list)
    for item in generate_dog_food(DRY_DOG_FOOD_FIELDS):
        products["dry_dog_food"].append(item)
    for item in generate_cat_food(DRY_CAT_FOOD_FIELDS):
        products["dry_cat_food"].append(item)

    print "dry dog food:", len(products["dry_dog_food"])
    print "dry cat food:", len(products["dry_cat_food"])

    fl = open("products.json", "w")
    json.dump(products, fl)
    fl.close()


if __name__ == "__main__":
    main()
    
    
        
