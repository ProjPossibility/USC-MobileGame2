#include <string>

class remoteTCPConnect
{
private:
	int mainSockID;
	bool disconnect;
	int setupSocket();
	char* data;
	char* data2;
	int length;
	int length2;
public:
	remoteTCPConnect();
	int setup(int receiveBufferLength);
	int tearDown();
	int parse(std::string message);
};
