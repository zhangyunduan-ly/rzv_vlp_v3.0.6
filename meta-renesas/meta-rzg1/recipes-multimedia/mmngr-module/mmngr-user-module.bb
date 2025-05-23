require include/rz-modules-common.inc

LICENSE = "CLOSED"
DEPENDS = "kernel-module-mmngr"
PN = "mmngr-user-module"
S = "${WORKDIR}/mmngr"
SRC_URI = "file://mmngr.tar.bz2"

# These modules are machine specific.
PACKAGE_ARCH = "${MACHINE_ARCH}"

sysroot_stage_all_append () {
    # add shared header files
    sysroot_stage_dir ${D}/usr/local/include/ ${SYSROOT_DESTDIR}${includedir}
    sysroot_stage_dir ${D}/usr/local/lib/ ${SYSROOT_DESTDIR}${libdir}
}

do_compile() {
    # Build shared library
    cd ${S}/if
    rm -rf ${S}/if/libmmngr.so*
    make all ARCH=arm
    # Copy shared library into shared folder
    cp -P ${S}/if/libmmngr.so* ${LIBSHARED}
}

do_install() {
    mkdir -p ${D}/usr/local/lib/ ${D}/usr/local/include

    # Copy shared library
    cp -P ${S}/if/libmmngr.so* ${D}/usr/local/lib/
    cd ${D}/usr/local/lib/
    # Copy shared header files
    cp -f ${BUILDDIR}/include/mmngr_user_public.h ${D}/usr/local/include
    cp -f ${BUILDDIR}/include/mmngr_user_private.h ${D}/usr/local/include
}

# Append function to clean extract source
do_cleansstate_prepend() {
        bb.build.exec_func('do_clean_source', d)
}

do_clean_source() {
    rm -f ${LIBSHARED}/libmmngr.so*
    rm -Rf ${BUILDDIR}/include/mmngr_user_public.h
    rm -Rf ${BUILDDIR}/include/mmngr_user_private.h
}

PACKAGES = "\
    ${PN} \
    ${PN}-dev \
"

FILES_${PN} += " \
    /usr/local/lib/libmmngr.so.* \
    ${libdir}/*.so \
    ${libdir}/*.la \
    /usr/local/include \
    /usr/local/include\*.h \
"

FILES_${PN}-dev += " \
    /usr/local/include \
    /usr/local/include/*.h \
    /usr/local/lib \
    /usr/local/lib/libmmngr.so \
"

RPROVIDES_${PN} += "mmngr-user-module"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP_${PN} += "libdir"
INSANE_SKIP_${PN}-dev += "libdir"

do_configure[noexec] = "1"

python do_package_ipk_prepend () {
    d.setVar('ALLOW_EMPTY', '1')
}

# Skip debug strip of do_populate_sysroot()
INHIBIT_SYSROOT_STRIP = "1"
