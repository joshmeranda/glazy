SRC:=$(shell find . -type f -name "*.kt")

VERSION=1.0.2
JAR=./entry/target/entry-${VERSION}-jar-with-dependencies.jar

BINDIR=/usr/bin/
LIBDIR=/usr/lib/glazy/

BIN=glazy
MVN=mvn

CP=cp --force
RM=rm --recursive --verbose --force

.PHONY: package install remove clean

${JAR}: ${SRC}
	${MVN} package

package:
	${MVN} package

install: ${JAR}
	mkdir -p ${LIBDIR}
	${CP} ${JAR} ${LIBDIR}
	${CP} ${BIN} ${BINDIR}

remove:
	${RM} ${LIBDIR}
	${RM} ${BINDIR}/${BIN}

clean:
	${MVN} clean
