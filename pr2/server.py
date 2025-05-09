import Pyro4 # type: ignore
# Define the remote object
@Pyro4.expose
class StringConcatenator:
    def concatenate(self, str1, str2):
        return str1 + str2
    
# Register the remote object with the Pyro4 daemon
def main():
    Pyro4.Daemon.serveSimple({
        StringConcatenator: "string.concatenator"
        },
        host="localhost",
        port=9090,
        ns=False
)
print("StringConcatenator server is running...")

if __name__ == "__main__":
    main()