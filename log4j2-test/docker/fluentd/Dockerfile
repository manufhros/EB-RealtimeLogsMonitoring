FROM fluent/fluentd:v1.16-debian

USER root
RUN gem install fluent-plugin-opensearch --no-document

COPY fluent.conf /fluentd/etc/

USER fluent
CMD ["fluentd", "-c", "/fluentd/etc/fluent.conf"]

