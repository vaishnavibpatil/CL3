import numpy as np
def fuzzy_union(A, B):
    return {key: max(A.get(key, 0), B.get(key, 0)) for key in set(A) | set(B)}

def fuzzy_intersection(A, B):
    return {key: min(A.get(key, 0), B.get(key, 0)) for key in set(A) & set(B)}

def fuzzy_complement(A):
    return {key: 1 - value for key, value in A.items()}

def fuzzy_difference(A, B):
    return {key: min(A.get(key, 0), 1 - B.get(key, 0)) for key in set(A) | set(B)}

def cartesian_product(A, B):
    return {(a, b): min(A[a], B[b]) for a in A for b in B}

def max_min_composition(R1, R2):
    result = {}
    for (a, b) in R1:
        for (c, d) in R2:
            if b == c:
                result[(a, d)] = max(result.get((a, d), 0), min(R1[(a, b)], R2[(c, d)]))
    return result

# Example Fuzzy Sets
A = {'x1': 0.2, 'x2': 0.5, 'x3': 0.8}
B = {'x2': 0.4, 'x3': 0.7, 'x4': 0.9}

# Operations
print("Union:", fuzzy_union(A, B))
print("Intersection:", fuzzy_intersection(A, B))
print("Complement of A:", fuzzy_complement(A))
print("Difference (A - B):", fuzzy_difference(A, B))    

# Cartesian Product-based Fuzzy Relation
R1 = cartesian_product(A, B)
R2 = cartesian_product(B, A)
print("Cartesian Product (Fuzzy Relation):", R1)

# Max-Min Composition
result_composition = max_min_composition(R1, R2)
print("Max-Min Composition:", result_composition)