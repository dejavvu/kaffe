/* jni_md.h
   Copyright (C) 2001, 2005 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

#ifndef __CLASSPATH_JNI_MD_H__
#define __CLASSPATH_JNI_MD_H__

#include <stdint.h>

/* Linkage and calling conventions. */
#if defined (_WIN32) || defined (__WIN32__) || defined (WIN32)

#define JNIIMPORT        __declspec(dllimport)
#define JNIEXPORT        __declspec(dllexport)

#define JNICALL          __stdcall

#else /* !( _WIN32 || __WIN32__ || WIN32) */

#define JNIIMPORT
#define JNIEXPORT
#define JNICALL

#endif /* !( _WIN32 || __WIN32__ || WIN32) */

typedef uint8_t  jboolean;
typedef int8_t   jbyte;
typedef uint16_t jchar;
typedef int16_t  jshort;
typedef int32_t  jint;
typedef int64_t  jlong;
typedef float    jfloat;
typedef double   jdouble;
typedef jint     jsize;

#endif /* __CLASSPATH_JNI_MD_H__ */
