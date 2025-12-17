# Dockerfile for MongoDB server
FROM mongo:latest

EXPOSE 27017

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD mongo --quiet --eval "db.adminCommand('ping')" || exit 1

