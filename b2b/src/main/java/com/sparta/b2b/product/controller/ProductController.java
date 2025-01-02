package com.sparta.b2b.product.controller;

import com.sparta.b2b.product.dto.request.ProductCreateRequest;
import com.sparta.b2b.product.dto.request.ProductUpdateRequest;
import com.sparta.b2b.product.dto.response.PageProductResponse;
import com.sparta.b2b.product.dto.response.ProductCreateResponse;
import com.sparta.b2b.product.dto.response.ProductSearchResponse;
import com.sparta.b2b.product.dto.response.ProductUpdateResponse;
import com.sparta.b2b.product.service.ProductService;
import com.sparta.common.annotation.CheckAuth;
import com.sparta.common.annotation.LoginMember;
import com.sparta.common.dto.MemberSession;
import com.sparta.common.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	@CheckAuth(role = Role.B2B)
	@PostMapping()
	public ResponseEntity<ProductCreateResponse> createProduct(
		@RequestPart(value = "requestDto") @Valid ProductCreateRequest request,
		@LoginMember(role = Role.B2B) MemberSession memberSession,
		@RequestPart(value = "images") List<MultipartFile> productImageFiles
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(productService.createProduct(memberSession.memberId(), request, productImageFiles));
	}


	@CheckAuth(role = Role.B2B)
	@PostMapping("/v2")
	public ResponseEntity<ProductCreateResponse> createProductV2(
		@RequestBody @Valid ProductCreateRequest request,
		@LoginMember(role = Role.B2B) MemberSession memberSession
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(productService.createProductV2(request, memberSession.memberId()));
	}


	@CheckAuth(role = Role.B2B)
	@GetMapping()
	public ResponseEntity<PageProductResponse> totalSearchProduct(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "asc") String orderBy,
		@RequestParam(defaultValue = "name") String sortBy,
		@LoginMember(role = Role.B2B) MemberSession memberSession
	) {
		PageProductResponse response = productService.totalSearchProduct(memberSession.memberId(), page, size, orderBy, sortBy);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(response);
	}

	@CheckAuth(role = Role.B2B)
	@GetMapping("/{id}")
	public ResponseEntity<ProductSearchResponse> searchProduct(
		@PathVariable Long id,
		@LoginMember(role = Role.B2B) MemberSession memberSession
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(productService.searchProduct(memberSession.memberId(), id));
	}

	@CheckAuth(role = Role.B2B)
	@PatchMapping("/{id}/stock-quantity")
	public ResponseEntity<ProductUpdateResponse> updateProduct(
		@PathVariable Long id,
		@LoginMember(role = Role.B2B) MemberSession memberSession,
		@RequestBody @Valid ProductUpdateRequest request
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(productService.updateProduct(memberSession.memberId(), id, request));
	}

	@CheckAuth(role = Role.B2B)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(
		@PathVariable Long id,
		@LoginMember(role = Role.B2B) MemberSession memberSession
	) {
		productService.deleteProduct(memberSession.memberId(), id);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT).build();
	}
}
