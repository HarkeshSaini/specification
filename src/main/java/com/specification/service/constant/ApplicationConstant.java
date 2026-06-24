package com.specification.service.constant;

public class ApplicationConstant {

   private ApplicationConstant() {
   }


   public static final String MSG_INTERNAL_SERVER_ERROR="E-AU-TS-C-50000001";
   public static final String MSG_MISSING_USER_PASSWORD="E-AU-TS-C-50000002";

   public static final String LOGIN_REQUEST_FOR_EMAIL = "Login request for email={}";
   public static final String AUTH_TOKEN_CREATED_SUCCESS_MESSAGE = "Successfully created token.";

   public static final String SECURITY_WEB_FILTER_CHAIN_CONFIGURED = "Configuring SecurityWebFilterChain with JWT filter";
   public static final String CREATING_BCRYPT_PASSWORD_ENCODER_BEAN = "Creating BCryptPasswordEncoder bean";
   public static final String BUILDING_TOKEN_RESPONSE_FOR_AUTH_ACCOUNT = "Building token response for authAccountId={}";
   public static final String TOKEN_TYPE_BEARER = "Bearer ";
   public static final long MILLISECONDS_TO_SECONDS_DIVISOR = 1000L;

   /**
    * HS256 signing secret (UTF-8); must be at least 32 bytes. Override only by changing this constant (or refactor to a vault) if required.
    */
   public static final String JWT_SECRET = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";

   /** Access-token time-to-live in milliseconds. */
   public static final long JWT_EXPIRATION_MS = 86400000L;

   public static final String JWT_SKIP_PROCESSING_FOR_PATH = "Skipping JWT processing for path={}";
   public static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
   public static final int AUTHORIZATION_BEARER_PREFIX_LENGTH = 7;
   public static final String JWT_REJECTED_FOR_PATH = "JWT rejected for {}: {}";
   public static final String RETURNING_UNAUTHORIZED = "Returning 401 Unauthorized";
   public static final String JWT_SECRET_MIN_LENGTH_ERROR = "ApplicationConstant.JWT_SECRET must be at least 32 bytes (256 bits) for HS256";

   public static final String JWT_VALIDATION_FAILED = "JWT validation failed: {}";
   public static final String JWT_CLAIM_EMAIL = "email";
   public static final String JWT_CLAIM_PROFILE_ID = "profileId";
   public static final String JWT_CLAIM_ROLE = "role";

   public static final String GET_API_WEBSITE_ME = "GET /api/website/me";
   public static final String GET_API_MANAGER_ME = "GET /api/manager/me";
   public static final String GET_API_MANAGER_WEBSITE_USERS = "GET /api/manager/website-users";
   public static final String GET_API_ADMIN_ME = "GET /api/admin/me";
   public static final String GET_API_SUPER_ADMIN_ME = "GET /api/super-admin/me";
   public static final String SUPER_ADMIN_PROFILE_FETCHED_SUCCESSFULLY = "Super admin profile fetched successfully.";
   public static final String ADMIN_PROFILE_FETCHED_SUCCESSFULLY = "Admin profile fetched successfully.";
   public static final String MANAGER_PROFILE_FETCHED_SUCCESSFULLY = "Manager profile fetched successfully.";
   public static final String WEBSITE_PROFILE_FETCHED_SUCCESSFULLY = "Website profile fetched successfully.";
   public static final String WEBSITE_USERS_FETCHED_SUCCESSFULLY = "Website users fetched successfully.";
   public static final String USER_CREATED_SUCCESSFULLY = "User created successfully.";
   public static final String USER_UPDATED_SUCCESSFULLY = "User updated successfully.";
   public static final String USER_FETCHED_SUCCESSFULLY = "User fetched successfully.";
   public static final String USER_LIST_FETCHED_SUCCESSFULLY = "Users fetched successfully.";
   public static final String USER_SOFT_DELETED_SUCCESSFULLY = "User deleted successfully.";
   public static final String ROLE_PARSE_ERROR = "Invalid role value: ";

   public static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
   public static final String AUTHENTICATION_SUCCESSFUL_FOR_EMAIL = "Authentication successful for email={}";
   public static final String ISSUED_ACCESS_TOKEN_FOR_ACCOUNT = "Issued access token for authAccountId={}, role={}";
   public static final String ROLE_CHECK_FAILED_REQUIRED = "Role check failed: required {}";
   public static final String REQUIRES_ROLE_PREFIX = "Requires role: ";
   public static final String ROLE_CHECK_FAILED_ADMIN = "Role check failed: required ADMIN";
   public static final String REQUIRES_ADMIN = "Requires admin";
   public static final String REQUIRES_USER_MANAGEMENT_ACCESS = "Requires admin or super admin";
   public static final String REQUIRES_SUPER_ADMIN = "Requires super admin";
   public static final String ROLE_CHECK_FAILED_USER_MANAGEMENT = "Role check failed: required user management access";
   public static final String ROLE_CHECK_FAILED_SUPER_ADMIN = "Role check failed: required SUPER_ADMIN";

   public static final String RESOLVING_ADMIN_PROFILE_FOR_CURRENT_USER = "Resolving admin profile for current user";
   public static final String RESOLVING_SUPER_ADMIN_PROFILE_FOR_CURRENT_USER = "Resolving super admin profile for current user";
   public static final String RESOLVING_MANAGER_PROFILE_FOR_CURRENT_USER = "Resolving manager profile for current user";
   public static final String LISTING_WEBSITE_USERS_MANAGER_FLOW = "Listing website users (manager flow)";
   public static final String RESOLVING_WEBSITE_USER_PROFILE_FOR_CURRENT_USER = "Resolving website user profile for current user";
   public static final String ADMIN_PROFILE_LABEL = "Admin profile";
   public static final String SUPER_ADMIN_PROFILE_LABEL = "Super admin profile";
   public static final String MANAGER_PROFILE_LABEL = "Manager profile";
   public static final String WEBSITE_USER_PROFILE_LABEL = "Website user profile";

   public static final String LOADING_ADMIN_PROFILE_ID = "Loading admin profile id={}";
   public static final String LOADING_SUPER_ADMIN_PROFILE_ID = "Loading super admin profile id={}";
   public static final String LOADING_MANAGER_PROFILE_ID = "Loading manager profile id={}";
   public static final String LOADING_WEBSITE_USER_PROFILE_ID = "Loading website user profile id={}";
   public static final String LISTING_ALL_WEBSITE_USERS = "Listing all website users";
   public static final String MAPPING_ADMIN_USER_TO_RESPONSE = "Mapping AdminUser id={} to response";
   public static final String MAPPING_SUPER_ADMIN_USER_TO_RESPONSE = "Mapping SuperAdminUser id={} to response";
   public static final String MAPPING_MANAGER_USER_TO_RESPONSE = "Mapping ManagerUser id={} to response";
   public static final String MAPPING_WEBSITE_USER_TO_RESPONSE = "Mapping WebsiteUser id={} to response";
   public static final String NETTY_NO_UNSAFE_CONFIG_KEY = "app.netty.no-unsafe";
   public static final String NETTY_NO_UNSAFE_SYSTEM_PROPERTY = "io.netty.noUnsafe";

   public static final String AUTHENTICATION_FAILED = "Authentication failed: {}";
   public static final String FORBIDDEN_MESSAGE = "Forbidden: {}";
   public static final String RESOURCE_NOT_FOUND = "Resource not found: {}";
   public static final String UNHANDLED_ERROR = "Unhandled error";
   public static final String ERROR_CODE_UNAUTHORIZED = "UNAUTHORIZED";
   public static final String ERROR_CODE_FORBIDDEN = "FORBIDDEN";
   public static final String ERROR_CODE_NOT_FOUND = "NOT_FOUND";
   public static final String ERROR_CODE_INTERNAL = "INTERNAL_ERROR";
   public static final String ERROR_CODE_CONFLICT = "CONFLICT";
   public static final String ERROR_CODE_BAD_REQUEST = "BAD_REQUEST";
   public static final String ERROR_CODE_TOO_MANY_REQUESTS = "TOO_MANY_REQUESTS";
   public static final String INVALID_CREDENTIALS = "Invalid credentials";
   public static final String ACCESS_DENIED = "Access denied";
   public static final String UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred";

   public static final String BLOG_LABEL = "Blog post";
   public static final String BLOG_CREATED_SUCCESSFULLY = "Blog post created successfully.";
   public static final String BLOG_UPDATED_SUCCESSFULLY = "Blog post updated successfully.";
   public static final String BLOG_FETCHED_SUCCESSFULLY = "Blog post fetched successfully.";
   public static final String BLOG_LIST_FETCHED_SUCCESSFULLY = "Blog posts fetched successfully.";
   public static final String BLOG_DELETED_SUCCESSFULLY = "Blog post deleted successfully.";
}
