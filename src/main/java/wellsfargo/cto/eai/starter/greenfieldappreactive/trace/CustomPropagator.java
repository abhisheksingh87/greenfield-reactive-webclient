package wellsfargo.cto.eai.starter.greenfieldappreactive.trace;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomPropagator implements TextMapPropagator {

    @Override
    public List<String> fields() {
        return Arrays.asList("customTraceId", "customSpanId", "customHeader");

    }

    @Override
    public <C> void inject(Context context, C carrier, TextMapSetter<C> setter) {
        SpanContext spanContext = Span.fromContext(context).getSpanContext();
        if (!spanContext.isValid()) {
            return;
        }
        String customHeaders = "host-name-customer-service";
        setter.set(carrier, "customTraceId", spanContext.getTraceId());
        setter.set(carrier, "customSpanId", spanContext.getSpanId());
        setter.set(carrier, "customHeader", customHeaders);
    }

    @Override
    public <C> Context extract(Context context, C carrier, TextMapGetter<C> getter) {
        String traceParent = getter.get(carrier, "customTraceId");
        if (traceParent == null) {
            return Span.getInvalid().storeInContext(context);
        }
        String spanId = getter.get(carrier, "customSpanId");
        String customHeader = getter.get(carrier, "customHeader");
        return Span.wrap(SpanContext.createFromRemoteParent(traceParent, spanId, TraceFlags.getSampled(),
                TraceState.builder().build())).storeInContext(context);
    }

}
