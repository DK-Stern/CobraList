export interface UserDto {
  id: number | null;
  name: string;
  email: string | null;
  authorities: string[]
}
