A. Requirements :
	1. Install docker desktop
	2. Install WSL
	3. Activate Hypervisor if turned off : bcdedit /set hypervisorlaunchtype auto
	4. Restart computer if necessary

B. Execution of database :
	1. Run containers : docker-compose up -d
	2. Verify status and list of containers : docker ps

C. Verification of PostgreSQL :
	1. With the name appearing for the service when using "docker ps", enter the bash shell of the service and access its database with :
	docker exec -it containerName psql -U userName -d dataBaseName
	notes : use the containerName appearing with docker ps, use the userName + dataBaseName + password used in the .env to log in to PostgreSQL