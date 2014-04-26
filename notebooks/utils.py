import pandas as pd

def load_population_data(flname):
	fl = open(flname)
	pop_data = dict()
	# skip headers
	next(fl)
	for ln in fl:
		if ln.strip() == "":
			continue
		zipcode, pop = ln.split(",")
		pop = int(pop)
		if zipcode in pop_data:
			pop_data[zipcode] = max(pop_data[zipcode], pop)
		else:
			pop_data[zipcode] = pop
	fl.close()
	pops = []
	zipcodes = []
	for z, p in pop_data.iteritems():
		zipcodes.append(z)
		pops.append(p)
	return pd.Series(data=pops, index=zipcodes)