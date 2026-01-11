package org.qosp.notes.ui.editor.markdown

import android.text.Editable
import android.text.Spanned
import android.text.style.ScaleXSpan
import io.noties.markwon.core.spans.StrongEmphasisSpan
import io.noties.markwon.editor.AbstractEditHandler
import io.noties.markwon.editor.MarkwonEditorUtils
import io.noties.markwon.editor.PersistedSpans

class StrongEmphasisHandler : AbstractEditHandler<StrongEmphasisSpan>() {
    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(StrongEmphasisSpan::class.java) { StrongEmphasisSpan() }
    }

    override fun handleMarkdownSpan(
        persistedSpans: PersistedSpans,
        editable: Editable,
        input: String,
        span: StrongEmphasisSpan,
        spanStart: Int,
        spanTextLength: Int
    ) {
        val delimiter = if (spanStart < input.length && input[spanStart] == '*') "**" else "__"
        val match = MarkwonEditorUtils.findDelimited(input, spanStart, delimiter)
        if (match != null) {
            // Apply Bold style
            editable.setSpan(
                persistedSpans.get(StrongEmphasisSpan::class.java),
                match.start(),
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Hide start delimiter
            editable.setSpan(
                ScaleXSpan(0f),
                match.start(),
                match.start() + 2,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Hide end delimiter
            editable.setSpan(
                ScaleXSpan(0f),
                match.end() - 2,
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<StrongEmphasisSpan> {
        return StrongEmphasisSpan::class.java
    }
}