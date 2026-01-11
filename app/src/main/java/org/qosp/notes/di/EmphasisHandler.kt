package org.qosp.notes.ui.editor.markdown

import android.text.Editable
import android.text.Spanned
import android.text.style.ScaleXSpan
import io.noties.markwon.core.spans.EmphasisSpan
import io.noties.markwon.editor.AbstractEditHandler
import io.noties.markwon.editor.MarkwonEditorUtils
import io.noties.markwon.editor.PersistedSpans

class EmphasisHandler : AbstractEditHandler<EmphasisSpan>() {
    override fun configurePersistedSpans(builder: PersistedSpans.Builder) {
        builder.persistSpan(EmphasisSpan::class.java) { EmphasisSpan() }
    }

    override fun handleMarkdownSpan(
        persistedSpans: PersistedSpans,
        editable: Editable,
        input: String,
        span: EmphasisSpan,
        spanStart: Int,
        spanTextLength: Int
    ) {
        val delimiter = if (spanStart < input.length && input[spanStart] == '*') "*" else "_"
        val match = MarkwonEditorUtils.findDelimited(input, spanStart, delimiter)
        if (match != null) {
            // Apply Italic style
            editable.setSpan(
                persistedSpans.get(EmphasisSpan::class.java),
                match.start(),
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Hide start delimiter
            editable.setSpan(
                ScaleXSpan(0f),
                match.start(),
                match.start() + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Hide end delimiter
            editable.setSpan(
                ScaleXSpan(0f),
                match.end() - 1,
                match.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun markdownSpanType(): Class<EmphasisSpan> {
        return EmphasisSpan::class.java
    }
}