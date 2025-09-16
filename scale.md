In order to handle around 10,000,000 users, the following be implemented:

1- Currently the OMDB Service is written in a reactive way, but the whole application can be implemented like that to
prevent blocking requests
2- Use Redis for distributed caching of OMDb responses
3- Containerize and orchestrate with Kubernetes
4- Use API Gateway in addition to a Load Balancer