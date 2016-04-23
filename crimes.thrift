namespace java com.bignerdranch.android.criminalintent.models.raw

typedef string uuid

typedef i64 timestamp

struct Crime {
  1: optional uuid id
  2: optional string title
  3: optional timestamp createdDate
  4: optional bool solved
  5: optional string suspect
}

service CrimeService {

  list<Crime> fetchAllCrimes();

  Crime getCrime(1: uuid id);

  # true if added, false if updated
  bool createOrUpdateCrime(1: Crime crime);

  # true if deleted
  bool deleteCrime(1: uuid id);

}
