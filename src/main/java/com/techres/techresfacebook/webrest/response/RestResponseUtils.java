package com.techres.techresfacebook.webrest.response;

import io.vavr.control.Try;
import lombok.val;

public class RestResponseUtils {

    @SuppressWarnings({"rawtypes"})
    public static <T> GenericBaseResponse<?> create(Try<T> result,
                                                    String successStatus, Integer successCode,
                                                    String failureStatus, Integer failureCode) {
        GenericBaseResponse.Meta meta = new GenericBaseResponse.Meta();
        if (result instanceof Try.Failure) {
            val failureResult = (Try.Failure) result;
            meta.setStatus(failureStatus);
            meta.setErrorCode(failureCode);
            meta.setErrorMessage(failureResult.getCause().getMessage());
            return new GenericBaseResponse<>(new EmptyDataObject(), meta);
        } else {
            val successResult = (Try.Success) result;
            meta.setStatus(successStatus);
            meta.setErrorCode(successCode);
            meta.setErrorMessage("");
            return new GenericBaseResponse<>(successResult.get(), meta);
        }
    }


    public static <T> GenericBaseResponse<?> createList(java.util.List<T> result,
                                                    String successStatus, Integer successCode) {
        GenericBaseResponse.Meta meta = new GenericBaseResponse.Meta();
        meta.setStatus(successStatus);
        meta.setErrorCode(successCode);
        meta.setErrorMessage("");
        return new GenericBaseResponse<>(result, meta);
    }
}
