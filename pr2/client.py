import Pyro4
# Connect to the remote object
def main():
    server = Pyro4.Proxy("PYRO:string.concatenator@localhost:9090")
    str1 = input("Enter the first string:")
    str2 = input("Enter the second string:")
    try:
        result = server.concatenate(str1, str2)
        print(f"Concatenated String: {result}")
    except Exception as e:
        print(f"An error occurred: {e}")
if __name__ == "__main__":
    main()