{{#posts}}
	<article>
		<h2>{{title}}</h2>
		<p>{{date}}</p>

		<div>
			{{{body}}}
		</div>

		<ol class="comments">
			{{> blog/posts/comments}}
		</ol>
	</article>
{{/posts}}