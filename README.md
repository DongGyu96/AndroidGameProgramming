# AndroidGameProgramming



1주차 	6월1일~6월7일 	- 게임 프레임 워크 제작 ( 완료 )
2주차 	6월8일~6월9일 	- 플레이어 움직임 구현 ( 완료 )
	6월10일~6월14일 	- UI 구현 ( 100% )
3주차	6월15일~6월19일 	- 몬스터 구현 및 총알 발사, 충돌 체크 ( 100% )
3주차	6월19일~6월21일 	- 보스몬스터 구현 및 플레이어 스킬 구현 ( 100% )
+ 사운드 ( 100% )
+ 하이스코어 씬 ( 100% )

6월 20일 현재 토요일 오전 2시 기준
보스몬스터의 공격패턴추가, 하이스코어씬 추가,
스테이지 적 구성 변경, 사운드 추가 남았습니다.

아이템같은 경우에는 금방 만들수있으나 게임에서 사용할 리소스를 구하지 못하여
부득이하게 추가하지 못했습니다. 스킬 아이콘과 동일한 아이콘으로 아이템을 만들었으나
UI와 구분하지를 못해서 빼게 되었습니다.

기존의 계획은 플레이가 가능한 전투기를 4개를 리소스를 구해서 4개를 띄울 생각이였는데
해당 리소스들이 사용하기 좋게 같은 크기로 나열된게 아니라 픽셀 오차가 계속 있어서
포토샵으로 하나하나 잘라내서 붙여줘야 하는데 시간이 이게 너무 오래 걸려서 
2종류만 현재 선택가능합니다.
마찬가지로 스킬이나 공격타입도 여러개로 하고 싶었는데 아이템도 아이템이였지만
리소스를 하나하나 잘라내야 하는 문제점때문에 넣지 못했습니다.

보스몹 2종류 일반몹 3종류 플레이가능전투기 2종류가 등장하고
스테이지는 보스가 2종류인만큼 2스테이지로 구성할 생각입니다

보스몹또한 리소스를 더이상 구할 수 없엇고
스텔스기 보스몹은 양 날개에 추가적인 오브젝트를 배치하여 부위 파괴 등이 가능하며
UFO 형태의 보스몹은 1페이즈, 2페이즈 형식이 가능하도록 만들었습니다.
 ============================================================
6월 20일 토요일 오후 6시20분 기준
모든 컨텐츠 구현 완료
하이스코어만 제작한다면 모두 구현이 완료됩니다.
보스 등장 전 경고메시지, 2스테이지 클리어 후 게임클리어 씬 추가,
보스 패턴 추가, 보스 사망 연출 추가, 사운드 추가

=============================================================
6월 21일 일요일 오전 3시
하이스코어씬을 추가하였으며 모든 작업이 완료되었습니다.

=========================================================
캐릭터는 2종류로
f117은 f22에 비해 공격력이 2배 강하지만 이동속도와 공격속도가 느린편이고
f22는 f117에 비해 공격력이 약하지만 이동속도와 공격속도가 빠른편입니다.
또한 캐릭터는 스킬을 하나씩 사용할 수 있는데 
f117의 경우는 랜덤한 지점에 폭발을 계속 일으켜서 공격을 주는 스킬이며
f22는 맵 전체 범위에 폭발을 한번 일으키는 스킬입니다.
해당 스킬 모두 충돌범위 안에 있는 적과 총알을 제거합니다.
ff17의 경우 보스에게는 데미지를 가하지 않도록 해두었습니다.

보스몹은 커다란 폭격기와 UFO 형태의 보스몹 두가지가 존재하며
커다란 폭격기는 양 날개에 추가적인 공격포인트를 가지고 있어
주기적으로 플레이어를 향해 총알을 발사합니다.
본체를 부수기 전에 양 날개를 파괴하여 시간은 좀 더 걸리지만 더 쉽게 잡을 수 있습니다.
또한 날개 한쪽이 부셔질때 모델링도 반영하도록 하였습니다.

UFO 형태의 보스몹은 처음 중앙을 향해 일정량 이상 공격하면
해치가 열리면서 다른 패턴을 사용하도록 하였습니다.

게임이 클리어되면 현재 플레이어의 타입에 따라서 다른 게임 클리어 화면을 보여주며
게임오버가 되거나 클리어할때 하이스코어에 반영합니다.
