#include <string>
#include <iostream>
#include <istream>
#include "remoteTCPConnect.h"


////g++ remoteMain.cpp remoteTCPConnect.cpp -o remote.out -lsocket -lnsl -lresolv

int main()
{
	remoteTCPConnect serv;
	serv.setup(64);
	return 0;
}
