docker exec -it keycloak-server /opt/keycloak/bin/kc.sh export --realm scoutinghub --users realm_file --file  full.json

docker cp keycloak-server:/opt/keycloak/conf/full.json .