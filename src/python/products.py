from collections import defaultdict

def load_products():
	products = defaultdict(list)
	fl = open("products.csv")

	# skip header
	next(fl)

	for ln in fl:
		cols = ln.strip().split(",")
		category = cols[0]
		item = {
			"category" : cols[0],
			"brand" : cols[1],
			"flavor" : cols[2],
			"size" : float(cols[3]),
			"per_unit_cost" : float(cols[4])
			}
		products[category].append(item)

	fl.close()

	return products

