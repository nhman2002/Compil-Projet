	.text
	.global _start
_start:
	add r4, sp, #0
	sub sp, sp, #1000
	push {fp, lr}
	add fp, sp, #4
	sub sp, sp, #12
	ldr r12, =#1
	mov r5, r12
	mov r12, r7
	mov r6, r12
	ldr r12, =#42
	mov r5, r12
	push {r0-r3}
	mov r0, r5
	bl _min_caml_print_int
	pop {r0-r3}
	ldr fp, [sp], #4
	bl min_caml_exit