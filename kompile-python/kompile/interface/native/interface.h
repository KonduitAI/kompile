/* Generated by Cython 0.29.23 */

#ifndef __PYX_HAVE__kompile__interface__native__interface
#define __PYX_HAVE__kompile__interface__native__interface

#include "Python.h"

#ifndef __PYX_HAVE_API__kompile__interface__native__interface

#ifndef __PYX_EXTERN_C
  #ifdef __cplusplus
    #define __PYX_EXTERN_C extern "C"
  #else
    #define __PYX_EXTERN_C extern
  #endif
#endif

#ifndef DL_IMPORT
  #define DL_IMPORT(_T) _T
#endif

__PYX_EXTERN_C PyObject *convert_pointer_to_numpy(long *, PyObject *);
__PYX_EXTERN_C handles *_init_pipeline(PyObject *);
__PYX_EXTERN_C PyObject *_run(handles *, PyObject *);
__PYX_EXTERN_C PyObject *_run_pipeline(handles *, PyObject *);

#endif /* !__PYX_HAVE_API__kompile__interface__native__interface */

/* WARNING: the interface of the module init function changed in CPython 3.5. */
/* It now returns a PyModuleDef instance instead of a PyModule instance. */

#if PY_MAJOR_VERSION < 3
PyMODINIT_FUNC initinterface(void);
#else
PyMODINIT_FUNC PyInit_interface(void);
#endif

#endif /* !__PYX_HAVE__kompile__interface__native__interface */
