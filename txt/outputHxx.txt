/*
WARNING: THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.

This file was generated from hxx.idl using "idltoc".
The idltoc tool is part of the RTI Data Distribution Service distribution.
For more information, type 'idltoc -help' at a command shell
or consult the RTI Data Distribution Service manual.
*/
#ifndef hxx_h
#define hxx_h

#ifndef rti_me_cpp_hxx
#include "rti_me_cpp.hxx"
#endif

#ifdef NDDS_USER_DLL_EXPORT
#if (defined(RTI_WIN32) || defined(RTI_WINCE))
/* If the code is building on Windows, start exporting symbols. */
#undef NDDSUSERDllExport
#define NDDSUSERDllExport __declspec(dllexport)
#endif
#else
#undef NDDSUSERDllExport
#define NDDSUSERDllExport
#endif
    struct space_ASeq;
    class space_ATypeSupport;
    class space_ADataWriter;
    class space_ADataReader;

    class space_A
    {
    public:
        typedef struct space_ASeq Seq;
        typedef space_ATypeSupport TypeSupport;
        typedef space_ADataWriter DataWriter;
        typedef space_ADataReader DataReader;
        CDR_Short a;

    };

    extern const char *space_ATYPENAME;

    REDA_DEFINE_SEQUENCE_STRUCT(space_ASeq, space_A);

    REDA_DEFINE_SEQUENCE_IN_C(space_ASeq, space_A);

    NDDSUSERDllExport extern RTI_BOOL
    space_A_initialize(space_A *sample)
    {
            CDR_Primitive_init_Short(&sample->a);


        return RTI_TRUE;
    }

    NDDSUSERDllExport extern RTI_BOOL
    space_A_finalize(space_A *sample)
    {
        UNUSED_ARG(sample);

        return RTI_TRUE;
    }
    struct space_BSeq;
    class space_BTypeSupport;
    class space_BDataWriter;
    class space_BDataReader;

    class space_B
    {
    public:
        typedef struct space_BSeq Seq;
        typedef space_BTypeSupport TypeSupport;
        typedef space_BDataWriter DataWriter;
        typedef space_BDataReader DataReader;
        CDR_Short a;

    };

    extern const char *space_BTYPENAME;

    REDA_DEFINE_SEQUENCE_STRUCT(space_BSeq, space_B);

    REDA_DEFINE_SEQUENCE_IN_C(space_BSeq, space_B);

    NDDSUSERDllExport extern RTI_BOOL
    space_B_initialize(space_B *sample)
    {
            CDR_Primitive_init_Short(&sample->a);


        return RTI_TRUE;
    }

    NDDSUSERDllExport extern RTI_BOOL
    space_B_finalize(space_B *sample)
    {
        UNUSED_ARG(sample);

        return RTI_TRUE;
    }
#ifdef NDDS_USER_DLL_EXPORT
#if (defined(RTI_WIN32) || defined(RTI_WINCE))
/* If the code is building on Windows, stop exporting symbols. */
#undef NDDSUSERDllExport
#define NDDSUSERDllExport
#endif
#endif

#endif /* hxx */
