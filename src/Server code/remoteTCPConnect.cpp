#include "remoteTCPConnect.h"
#include <iomanip>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/wait.h>
remoteTCPConnect::remoteTCPConnect()
{
	disconnect=false;
	data = NULL;
	data2 = NULL;
}
int remoteTCPConnect::setup(int receiveBufferLength)
{
	int newSock = 0;
	int newSock2 = 0;
	int rcvFlag = 0;
	int lengthLo = receiveBufferLength;
	data = new char[lengthLo];
	data2 = new char[lengthLo];
	//char* rcvBuffer = new char[lengthLo];
	std::string contents;
	struct sockaddr_storage their_addr;
	struct sockaddr_storage their_addr2;
	socklen_t sin_size;	
	socklen_t sin_size2;
	printf("\n\n\n");

	mainSockID = setupSocket();
	if(mainSockID == -1)
	{
		printf("error setting up socket \n");
		return -1;
	}	
	
	while(true)
	{
		//receive first message
		sin_size = sizeof their_addr;
		newSock = accept(mainSockID, (struct sockaddr *)&their_addr, &sin_size);
		//std::cout<<"Connected.\n";
		if(newSock == -1)
		{
			perror("accept");
			continue;
		}
		rcvFlag = recv(newSock, data, lengthLo-1, 0);
		//std::cout<<"Receiving...\n";
		if(rcvFlag == -1)
		{
			perror("receive");
			exit(1);
		}
		printf("The remote server has received information from the client\n");
		//std::cout<<"Reception confirmed.\n";
		//data[rcvFlag] = '\0';
		
		length=rcvFlag;
		std::cout<<"received 1 msg: '"<<data<<"' length: "<<length<<"\n";
		//data=rcvBuffer;
		//rcvBuffer = new char[lengthLo];
		
		//receive second message
		sin_size2 = sizeof their_addr2;
		newSock2 = accept(mainSockID, (struct sockaddr *)&their_addr2, &sin_size2);
		//std::cout<<"Connected.\n";
		if(newSock2 == -1)
		{
			perror("accept");
			continue;
		}
		rcvFlag = recv(newSock2, data2, lengthLo-1, 0);
		//std::cout<<"Receiving...\n";
		if(rcvFlag == -1)
		{
			perror("receive");
			exit(1);
		}
		printf("The remote server has received information from the client\n");
		//std::cout<<"Connection Confirmed.\n";
		//data2[rcvFlag] = '\0';
		
		length2=rcvFlag;
		std::cout<<"received 2nd msg: '"<<data2<<"' length: "<<length2<<"\n";
		//data2=rcvBuffer;
		
		
		//send data
		if (send(newSock, data2, length2, 0) == -1)
			perror("send");
		if (send(newSock2, data, length, 0) == -1)
			perror("send");
		if (send(newSock, "pick",5,0) == -1)
			perror("send");
		
		std::cout<<"data sent to clients... \n";

		
		//fcntl(newSock, F_SETFL, O_NONBLOCK);
		//fcntl(newSock2, F_SETFL, O_NONBLOCK);
		
		while(true)
		{
			//std::cout<<"sleeping..\n";
			//sleep(1);
			std::cout<<"waking...\n";
			rcvFlag = recv(newSock2, data2, lengthLo-1, 0);
			if(rcvFlag>0)
			{
				send(newSock, data2, rcvFlag, 0);
			}
			std::cout<<"player 2 hit...\n";
			rcvFlag = recv(newSock, data, lengthLo-1, 0);
			if(rcvFlag>0)
			{
				send(newSock2, data, length, 0);
			}
			std::cout<<"player 1 hit...\n";
		}

		
		close(newSock);
		close(newSock2);
		
		delete data;
		data = NULL;
		delete data2;
		data2 = NULL;
		/*if(!fork())//if(child)
		{
			close(mainSockID);
			rcvFlag = recv(newSock, rcvBuffer, lengthLo-1, 0);
			//std::cout<<"Receiving...\n";
			if(rcvFlag ==-1)
			{
				perror("receive");
				exit(1);
			}
			printf("The remote server has received information from the remote server\n");
			//std::cout<<"Reception confirmed.\n";
			rcvBuffer[rcvFlag] = '\0';
			if(data == NULL)
			{
				std::cout<<"received 1 msg\n";
				length=lengthLo;
				data=rcvBuffer;
				while(true)
				{
					std::cout<<"sleeping\n";
					sleep(1);
					if(data2 != NULL)
						break;
				}
				if (send(newSock, data2, length2, 0) == -1)
					perror("send");
				delete data2;
				data2 = NULL;
			}
			else
			{
				std::cout<<"received 2nd msg\n";
				length2=lengthLo;
				data2=rcvBuffer;
				if (send(newSock, data, length, 0) == -1)
					perror("send");
				delete data;
				data = NULL;
			}

			close(newSock);

			if(disconnect)
				break;
		}//if(!fork())*/
		//rcvBuffer = new char[lengthLo];
	}//while(true)
	close(mainSockID);
}//setup(int)

int remoteTCPConnect::setupSocket()
{
	int sock = 0;
	int TCPsock = 22061;
	int yes=1;
	int status = 0;
	int bindID =0;
	int listenID=0;
	struct addrinfo hints;
	struct addrinfo *res, *target; //points to results

	memset(&hints, 0, sizeof hints); //hints is empty
	hints.ai_family = AF_UNSPEC; //IPv4 or v6
	hints.ai_socktype = SOCK_STREAM; //TCP
	hints.ai_flags = AI_PASSIVE; //autofill IP addr

	char* name = (char*) malloc(64);
	int hostFlag=0;
	//get the host name of the server the code is running on
	hostFlag=gethostname(name,64);
	if(hostFlag == -1)
	{
    		perror("getting hostname");
    		exit(1);
	}

	if ((status = getaddrinfo(name, "22061", &hints, &res)) != 0) 
	{
    		fprintf(stderr, " getaddrinfo error: %s\n", gai_strerror(status));
    		exit(1);
	}

	//loop through the linked list provided by getaddrinfo and bind the first one I can
	for(target = res; target != NULL; target=target->ai_next)	{
		if((sock = socket(target->ai_family, target->ai_socktype, target->ai_protocol))==-1)
		{
			std::cout<<" Error creating socket! \n";
			perror("socket: ");
			continue;//exit(1);
		}
		if(setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&yes,sizeof(int)) == -1)
		{
			perror("setsockopt");
			exit(1);
		}
		if(bind(sock,target->ai_addr,target->ai_addrlen) == -1)
		{
			close(sock);
			perror("bind");
			continue;
		}
		break;
	}
	if(target == NULL)
	{
		fprintf(stderr, "failed to bind socket\n");
		exit(2);
	}
	
	listenID = listen(sock,5);
	if(listenID ==-1)
	{
		std::cout<<" Error listening on socket! \n";
		perror("listen: ");
		exit(1);
	}

	char* ip4;
	ip4 = (char*) malloc(64);
	struct sockaddr_in *ipv4 = (struct sockaddr_in *)target->ai_addr;
	void* addr;
	int portNumber;
	portNumber = ntohs(ipv4->sin_port);
	addr= &(ipv4->sin_addr);
	inet_ntop(target->ai_family,addr, ip4, target->ai_addrlen);
	printf("The remote server has TCP port number %i and IP address %s\n",portNumber,ip4);
	freeaddrinfo(res);
	return sock;
}
int remoteTCPConnect::tearDown()
{
	disconnect=true;
}
int remoteTCPConnect::parse(std::string data)
{
	std::cout<<" Server received message: '"<<data<<"'"<<std::endl;
	return 0;
}
