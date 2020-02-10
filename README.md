# Kalah
Java RESTful Web Service that runs a game of 6-stone Kalah.

#### Kalah Rules
Each of the two players has **six pits** in front of him/her. To the right of the six pits, each player has a larger pit, his
Kalah or house.
At the start of the game, six stones are put in each pit.
The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in
each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last
stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other
player's turn.
When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.
The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps
them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

#### Stack
* Java 8
* Spring Boot
* Commons Lang/Collections
* JUnit + Spring Boot Test
* Tomcat 8
* Docker
* Azure Kubernetes Service (AKS)

#### Sequence Diagram
![Start Game](https://github.com/MithunKiniStar/Kalah/blob/master/Sequence%20Diagram/startGame().png)
![Make Move](https://github.com/MithunKiniStar/Kalah/blob/master/Sequence%20Diagram/makeMove.png)


#### API

Web Service is deployed on Azure Kubernetes Service (AKS).

- Swagger API documentation: http://kalah-game.eastus.cloudapp.azure.com:8080/swagger-ui.html

- Create Game: 

```
curl --header "Content-Type: application/json" --request POST http://kalah-game.eastus.cloudapp.azure.com:8080/games
```

- Make a move:
```
curl --header "Content-Type: application/json" --request PUT http://kalah-game.eastus.cloudapp.azure.com:8080/games/{gameId}/pits/{pitId}
```

# How to run the Kalah app
If you want to run project on your local machine set "local" profile (Set environment variables = spring.profiles.active=local) and type the following command from the root directory:

```
mvn clean install
```

# How to deploy Kalah app in Azure Kubernetes Service(AKS)
* Clone application from github https://github.com/MithunKiniStar/Kalah.git
* To build docker image uncomment spotify plugin in pom.xml
* Then build app using maven command - "mvn clean install". This will build docker image in local.
* For Azure Kubernetes Service please follow the prerequisites from the following link
  https://docs.microsoft.com/en-us/azure/dev-spaces/quickstart-java#prerequisites
  
#### Deploying docker image into Azure Kubernetes Service
```
az group create -l eastus -n kalahGroup
```   
#### Creating a container registry
```
az acr create -g kalahGroup -n kalahRegistry --sku Basic --admin-enabled
  -- Output = "loginServer": "kalahregistry.azurecr.io",
```  
#### Logging into the container registry
```
az acr login -n kalahRegistry
```
#### Uploading images to a container registry
```
docker tag <DOCKER ID>/kalah:0.0.1-SNAPSHOT kalahregistry.azurecr.io/kalah-service:vLATEST
docker push kalahregistry.azurecr.io/kalah-service:vLATEST
```  
#### Creating a Kubernetes cluster on AKS
```  
az aks create -g kalahGroup -n kalahCluster
```  
#### Merge the credentials of your cluster into your current Kubernetes configuration
```  
az aks get-credentials -g kalahGroup -n kalahCluster
```  
#### Run the following command to check the status of the available nodes in your AKS cluster
```  
kubectl get nodes
```  
#### Storing registry credentials in a secret
#### To be able to pull the images from your Azure container registry, the credentials of your registry must be added to your service through a secret.
#### View the password for your Azure container registry:
```  
az acr credential show -n kalahRegistry --query "passwords[0].value" -o tsv
--Output = RrmVvmXdCUxOrYFoaOln+WL1nTJ2=kka
```  
#### Use the kubectl create secret docker-registry command to create a secret to hold your registry credentials.
```  
kubectl create secret docker-registry kalahsecret --docker-server=kalahregistry.azurecr.io --docker-username=kalahRegistry --docker-password=RrmVvmXdCUxOrYFoaOln+WL1nTJ2=kka --docker-email=kinimithun@gmail.com  
```  
#### Deploying microservices to AKS
``` 
kubectl create -f C:\Users\Mithun.Kini\Desktop\Docker\Kalah\kubernetes.yaml
``` 
#### Run the following command to check the status of your pods:
``` 
kubectl get pods
``` 
#### Making requests to the microservices
#### List all the deployed services
``` 
kubectl get services
``` 
#### View the information of the system service to see its EXTERNAL-IP address:
``` 
kubectl get service/kalah-service
``` 
You can either invoke kalah app via external app or add the DNS name to the Kalah service using azure portal
Ex: http://kalah-game.eastus.cloudapp.azure.com:8080/games
   or http://52.150.55.87:8080/games
#### To give permission for Kubernetes dashboard
``` 
az aks browse --resource-group kalahGroup --name kalahCluster
``` 
