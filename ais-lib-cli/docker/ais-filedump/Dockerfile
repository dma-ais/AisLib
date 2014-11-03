FROM tbsalling/javabox

RUN mkdir -p /data
RUN wget --progress=bar "https://dma.ci.cloudbees.com/job/AisLib/lastSuccessfulBuild/artifact/*zip*/archive.zip" -O /archive.zip
RUN unzip /archive.zip -d /
ADD ./start.sh /start.sh
ENV SOURCES "sourceName=1.2.3.4"
ENV DIRECTORY /data/
CMD ["/bin/bash", "/start.sh"]
