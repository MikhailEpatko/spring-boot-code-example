FROM gitlab.natalie-tours.ru:4567/helpdesk/microservice_base_image:latest

ADD . /app
WORKDIR /app
EXPOSE 17500

ENV ENV1=ENV1 \
    ENV2=ENV2

CMD []
ENTRYPOINT ["/app/run.sh"]
