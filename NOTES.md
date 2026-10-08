# NOTES

## Summary of changes
Details are in the handwritten notes in `handwritten/`.

1. **BUG 1: SQL precedence (SQL/backend):** Added brackets around the title/description `OR` in the search query, so the `archived` and `status` filters apply to every row. Fixed in the repository query, `db/queries/search_tasks.sql` and both Oracle queries.
2. **BUG 2: Hidden `Thread.sleep` (backend):** Removed an artificial delay that made short searches slow.
3. **BUG 3: Pagination input (backend):** Clamped `page` (minimum 1) and `pageSize` (1 to 100) so bad values no longer crash the endpoint.
4. **BUG 4: Invalid status (backend):** Returns 400 with an error message instead of a 500.
5. **BUG 5: Stale requests (frontend):** Added `AbortController` cleanup in `useTasks`, and fixed the error state that left "Loading..." showing forever.
6. **BUG 6: Stuck page (frontend):** Page resets to 1 when the search or status changes.

## What I chose not to change
- Debounce on the search box: cancelling already prevents wrong results.
- Escaping `%` and `_` in the search term.
- Hardcoded CORS origin and the open H2 console.
- Oracle `ROWNUM` pagination style.

## Biggest remaining risk
All matching rows are loaded into memory and sliced in Java. It is fine for 49 rows but will not scale.

## Tools and AI used
I used Claude to help read the code and explain the bugs, and to draft the fixes. I ran the app, reproduced each bug, checked every fix myself, and wrote the handwritten explanations myself.

## Assumptions
The repo name was not specified, so I used `fullstack-patch-exercise`.
