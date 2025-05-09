import numpy as np
from sklearn.neural_network import MLPRegressor
from sklearn.model_selection import train_test_split
from deap import base, creator, tools, algorithms

# Sample data (replace with your actual data)
X = np.random.rand(100, 5)  # Features
y = np.random.rand(100)     # Target variable

# Split data into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Define the fitness function
def evaluate(individual):
    hidden_layer_sizes = tuple(map(int, individual))
    model = MLPRegressor(hidden_layer_sizes=hidden_layer_sizes, max_iter=1000, random_state=42)
    model.fit(X_train, y_train)
    mse = np.mean((model.predict(X_test) - y_test) ** 2)
    return mse,

# Genetic Algorithm setup
creator.create("FitnessMin", base.Fitness, weights=(-1.0,))
creator.create("Individual", list, fitness=creator.FitnessMin)

toolbox = base.Toolbox()
toolbox.register("attr_int", np.random.randint, 10, 100)
toolbox.register("individual", tools.initRepeat, creator.Individual, toolbox.attr_int, n=2)
toolbox.register("population", tools.initRepeat, list, toolbox.individual)

toolbox.register("mate", tools.cxTwoPoint)
toolbox.register("mutate", tools.mutPolynomialBounded, low=10, up=100, eta=1.0, indpb=0.2)
toolbox.register("select", tools.selTournament, tournsize=3)
toolbox.register("evaluate", evaluate)

# Run the Genetic Algorithm
population = toolbox.population(n=50)
algorithms.eaSimple(population, toolbox, cxpb=0.7, mutpb=0.2, ngen=20, verbose=True)

# Get the best individual
best_individual = tools.selBest(population, k=1)[0]
print("Best individual:", best_individual)
print("Best fitness:", evaluate(best_individual)[0])
